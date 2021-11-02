package fr.hygon.sheepwars.sheeps;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Sheep;

public abstract class CustomSheep extends Sheep {
    private final ServerPlayer launcher;
    private boolean hasTouchedGround = false;
    public int ticksOnGround = -1;

    public CustomSheep(ServerPlayer launcher) {
        super(EntityType.SHEEP, launcher.getLevel());
        this.launcher = launcher;
    }

    @Override
    protected void customServerAiStep() {

    }

    public ServerPlayer getLauncher() {
        return launcher;
    }

    @Override
    public void tick() {
        super.tick();

        if(!hasTouchedGround && isOnGround()) {
            ticksOnGround = 0;
            hasTouchedGround = true;
        }

        if(hasTouchedGround) {
            ticksOnGround++;
        }
    }
}
