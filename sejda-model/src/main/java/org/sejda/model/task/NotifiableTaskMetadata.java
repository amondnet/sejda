/*
 * Created on 28/ott/2011
 * Copyright 2011 by Andrea Vacondio (andrea.vacondio@gmail.com).
 * 
 * This file is part of the Sejda source code
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sejda.model.task;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.sejda.model.exception.TaskCancelledException;

/**
 * An immutable set of metadata related to the task the event is notifying about.
 * 
 * @author Andrea Vacondio
 * 
 */
public class NotifiableTaskMetadata implements Serializable {

    private static final long serialVersionUID = -6423865557633949211L;
    /**
     * Null object pattern
     */
    public static final NotifiableTaskMetadata NULL = new NullNotifiableTaskMetadata();
    private UUID taskIdentifier;
    private String qualifiedName;
    private BaseTask<?> task;

    private NotifiableTaskMetadata() {
        // empty constructor
    }

    public NotifiableTaskMetadata(BaseTask<?> task) {
        if (task == null) {
            throw new IllegalArgumentException("No task given, unable to create notifiable metadata.");
        }
        this.taskIdentifier = UUID.randomUUID();
        this.qualifiedName = task.getClass().getName();
        this.task = task;
    }

    /**
     * @return the identifier of the task the event is notifying about.
     */
    public UUID getTaskIdentifier() {
        return taskIdentifier;
    }

    /**
     * @return the qualified name of the task the event is notifying about.
     */
    public String getQualifiedName() {
        return qualifiedName;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(taskIdentifier).append(qualifiedName).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof NotifiableTaskMetadata)) {
            return false;
        }
        NotifiableTaskMetadata meta = (NotifiableTaskMetadata) other;
        return new EqualsBuilder().append(taskIdentifier, meta.taskIdentifier)
                .append(qualifiedName, meta.qualifiedName).isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("taskIdentifier", taskIdentifier)
                .append("qualifiedName", qualifiedName).toString();
    }

    public void stopTaskIfCancelled() throws TaskCancelledException {
        task.stopTaskIfCancelled();
    }

    /**
     * Null object pattern providing empty behavior.
     * 
     * @author Andrea Vacondio
     * 
     */
    private static class NullNotifiableTaskMetadata extends NotifiableTaskMetadata {

        private static final long serialVersionUID = 6788562820506828221L;

        @Override
        public UUID getTaskIdentifier() {
            return null;
        }

        @Override
        public String getQualifiedName() {
            return StringUtils.EMPTY;
        }
    }
}
