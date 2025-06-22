package com.samsthenerd.monthofswords.neoforge.xplat;

import com.samsthenerd.monthofswords.xplat.CAttachmentTarget;
import com.samsthenerd.monthofswords.xplat.CAttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

import java.util.Optional;
import java.util.function.UnaryOperator;

public record NFAttachmentTarget(IAttachmentHolder holder) implements CAttachmentTarget {
    @Override
    public <T> Optional<T> getAttached(CAttachmentType<T> attType) {
        return holder.getExistingData(((NFAttachmentType<T>)attType).attType());
    }

    @Override
    public <T> Optional<T> modifyAttached(CAttachmentType<T> attType, UnaryOperator<Optional<T>> modifier) {
        var attTypeNF = ((NFAttachmentType<T>)attType).attType();
        Optional<T> prevData = holder.getExistingData(attTypeNF);
        Optional<T> newData = modifier.apply(prevData);
        if(newData.isEmpty()){
            holder.removeData(attTypeNF);
        } else {
            holder.setData(attTypeNF, newData.get());
        }
        return newData;
    }

    @Override
    public Object getActualTarget() {
        return holder;
    }
}
