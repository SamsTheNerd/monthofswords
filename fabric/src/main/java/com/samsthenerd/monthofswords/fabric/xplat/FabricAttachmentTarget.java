package com.samsthenerd.monthofswords.fabric.xplat;

import com.samsthenerd.monthofswords.xplat.CAttachmentTarget;
import com.samsthenerd.monthofswords.xplat.CAttachmentType;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;

import java.util.Optional;
import java.util.function.UnaryOperator;

public record FabricAttachmentTarget(AttachmentTarget target) implements CAttachmentTarget {
    @Override
    public <T> Optional<T> getAttached(CAttachmentType<T> attType) {
        var attTypeFab = ((FabricAttachmentType<T>)attType).attTypeFabric();
        if(target.hasAttached(attTypeFab)) return Optional.of(target.getAttached(attTypeFab));
        return Optional.empty();
    }

    @Override
    public <T> Optional<T> modifyAttached(CAttachmentType<T> attType, UnaryOperator<Optional<T>> modifier) {
        var attTypeFab = ((FabricAttachmentType<T>)attType).attTypeFabric();
        Optional<T> oldData = target.hasAttached(attTypeFab) ? Optional.of(target.getAttached(attTypeFab)) : Optional.empty();
        var newData = modifier.apply(oldData);
        if(newData.isEmpty()){
            target.removeAttached(attTypeFab);
        } else {
            target.setAttached(attTypeFab, newData.get());
        }
        return newData;
    }

    @Override
    public Object getActualTarget() {
        return target;
    }
}
