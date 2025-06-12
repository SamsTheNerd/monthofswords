package com.samsthenerd.monthofswords.render;

import com.samsthenerd.monthofswords.utils.ColorUtils;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper.Argb;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class EchoMidVC extends MiddleVertexConsumer{

    private float time; // i mean, this shouldn't persist across frames?
    private Entity ent;

    public EchoMidVC(VertexConsumer innerConsumer, Entity ent) {
        super(innerConsumer);
        this.ent = ent;
        this.time = Util.getMeasuringTimeMs()/1000f;
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) {
//        double color
        double t = MathHelper.floorMod(time, 4f);
        t = t < 2 ? t / 4 : 1-t /4;
//        Vec3d xDot = new Vec3d(4+2,2,1).normalize();
//        Vec3d yDot = new Vec3d(2,4+2*t,3).normalize();
//        Vec3d zDot = new Vec3d(3,1,4+3).normalize();
//        double rd = MathHelper.floorMod(lastVecWritten.dotProduct(xDot) - time/2f, 1.0);
//        double gd = MathHelper.floorMod(lastVecWritten.dotProduct(yDot) + time/20f, 1.0);
//        double bd = MathHelper.floorMod(lastVecWritten.dotProduct(zDot) + time/20f, 1.0);
        Vec3d d = new Vec3d(1,3,1).normalize();
        double hd = MathHelper.floorMod(lastVecWritten.dotProduct(d) - time, 1.0);
//        double h = 0;
//        if( hd > 0.99){
//            h = hd - 0.1;
//        } else if( hd < 0.01){
//            h = hd+.39;
//        } else {
//            h = (0.5f + 0.07f * hd);
//        }
        double h = (0.47f + 0.1f * hd);
        int argb = ColorUtils.HSBtoRGB((float)h, 1f, 1f);
//        this.innerConsumer.color(red, green, blue, alpha);
        this.innerConsumer.color(Argb.withAlpha(128, argb));
//        this.innerConsumer.color(
//            0 + (int)(rd * 30),
//            245 + (int)(gd * 20),
//            245 + (int)(bd * 10),
//            Math.min(100, alpha));
        return this;
    }

    @Override
    public VertexConsumer light(int u, int v) {
        this.innerConsumer.light(LightmapTextureManager.MAX_LIGHT_COORDINATE);
        return this;
    }

    @Override
    public VertexConsumer vertex(float x, float y, float z) {
//        double newX = (0.9 + 0.1 * Math.abs(Math.sin(time/2f))) * x;
//        double newY = (0.9 + 0.1 * Math.random()) * y;
//        double newZ = z;

//        Random rand = Random.create((long)time * (new Vec3d(x,y,z).hashCode()));
////        double randish = 0 * rand.nextFloat();
//        double randish = 1;
//        double speed = 0.1;
//        double newX = randish*(0.01 *  Math.sin(speed * x*time/2f)) + x;
//        double newY = randish*(0.01 * (Math.cos(speed * y*time/2f))) + y;
//        double newZ = randish*(0.01 * (Math.sin(speed * z*time/1.5f))) + z;
//        lastVecWritten = new Vec3d(newX,newY,newZ);
//        this.innerConsumer.vertex((float)newX, (float)newY, (float)newZ);
//        return this;
        return super.vertex(x,y,z);
    }
}
