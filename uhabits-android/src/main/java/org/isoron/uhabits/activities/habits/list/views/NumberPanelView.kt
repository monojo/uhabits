/*
 * Copyright (C) 2016 Álinson Santos Xavier <isoron@gmail.com>
 *
 * This file is part of Loop Habit Tracker.
 *
 * Loop Habit Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Loop Habit Tracker is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.isoron.uhabits.activities.habits.list.views

import android.content.*
import android.util.*
import org.isoron.uhabits.core.utils.*
import org.isoron.uhabits.utils.AttributeSetUtils.*
import org.isoron.uhabits.utils.PaletteUtils.*
import java.util.*

class NumberPanelView(
        context: Context
) : ButtonPanelView<NumberButtonView>(context) {

    var values = DoubleArray(0)
        set(values) {
            field = values
            setupButtons()
        }

    var threshold = 0.0
        set(value) {
            field = value
            setupButtons()
        }

    var color = 0
        set(value) {
            field = value
            setupButtons()
        }

    var units = ""
        set(value) {
            field = value
            setupButtons()
        }

    var onInvalidEdit: () -> Unit = {}
        set(value) {
            field = value
            setupButtons()
        }

    var onEdit: (Long) -> Unit = {}
        set(value) {
            field = value
            setupButtons()
        }

    constructor(ctx: Context, attrs: AttributeSet) : this(ctx) {
        val paletteColor = getIntAttribute(ctx, attrs, "color", 0)
        color = getAndroidTestColor(paletteColor)
        buttonCount = getIntAttribute(ctx, attrs, "button_count", 5)
        threshold = getIntAttribute(ctx, attrs, "threshold", 1).toDouble()
        units = getAttribute(ctx, attrs, "unit", "min") ?: ""
        if (isInEditMode) initEditMode()
    }

    fun initEditMode() {
        val values = DoubleArray(buttonCount)
        values.map { Random().nextDouble() * (threshold * 3) }
        this.values = values
    }

    override fun createButton() = NumberButtonView(context)

    @Synchronized
    override fun setupButtons() {
        val day = DateUtils.millisecondsInOneDay
        val today = DateUtils.getStartOfToday()

        buttons.forEachIndexed { index, button ->
            val timestamp = today - index * day
            button.value = when {
                index + dataOffset < values.size -> values[index + dataOffset]
                else -> 0.0
            }
            button.color = color
            button.threshold = threshold
            button.units = units
            button.onEdit = { onEdit(timestamp) }
            button.onInvalidEdit = { onInvalidEdit() }
        }
    }
}