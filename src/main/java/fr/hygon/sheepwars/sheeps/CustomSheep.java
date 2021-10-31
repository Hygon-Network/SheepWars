package fr.hygon.sheepwars.sheeps;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Sheep;

public abstract class CustomSheep extends Sheep {
    private final ServerPlayer launcher;

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
}
