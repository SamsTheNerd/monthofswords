package com.samsthenerd.monthofswords.xplat;

import java.util.Optional;
import java.util.function.UnaryOperator;

public interface CAttachmentTarget{
    <T> Optional<T> getAttached(CAttachmentType<T> attType);

    <T> Optional<T> modifyAttached(CAttachmentType<T> attType, UnaryOperator<Optional<T>> modifier);

    Object getActualTarget();
}
