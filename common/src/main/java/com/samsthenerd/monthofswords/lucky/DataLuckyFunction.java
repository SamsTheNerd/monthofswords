package com.samsthenerd.monthofswords.lucky;

import com.samsthenerd.monthofswords.SwordsMod;
import net.minecraft.command.CommandExecutionContext;
import net.minecraft.command.ReturnValueConsumer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.server.function.MacroException;
import net.minecraft.server.function.Procedure;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface DataLuckyFunction extends LuckyFunction{

    default int attack(LivingEntity target, LivingEntity attacker){
        MinecraftServer server = attacker.getServer();
        if(server == null) return 0; // this should never happen probably
        CommandFunctionManager funMan = server.getCommandFunctionManager();
        ServerCommandSource source = attacker.getCommandSource()
            .withSilent()
            .withLevel(2); // can do most stuff, just not server management.
        CommandFunction<ServerCommandSource> command = getCommand(funMan);
        if(command == null) return 0;
        NbtCompound macroSubst = new NbtCompound();
        macroSubst.putString("lucky_target", target.getUuid().toString());
        macroSubst.putString("lucky_attacker", attacker.getUuid().toString());
        try{
            Procedure<ServerCommandSource> procedure = command.withMacroReplaced(macroSubst, funMan.getDispatcher());
            CommandManager.callWithContext(source, context -> CommandExecutionContext.enqueueProcedureCall(context, procedure, source, ReturnValueConsumer.EMPTY));

        } catch (MacroException e) {
            SwordsMod.LOGGER.error("Lucky Function Macro Error: " + e);
            return 0;
        }
        return getItemDamage();
    }

    int getItemDamage();

    @Nullable
    CommandFunction<ServerCommandSource> getCommand(CommandFunctionManager funMan);

    default boolean fromDataDriven(){
        return true;
    }

    record DataIdLuckyFunction(Identifier functionId, int rarityWeight, LuckyModifier modifier, Integer itemDamage) implements DataLuckyFunction{

        @Override
        public int getWeight(double luck){
            return modifier().modify(rarityWeight(), luck);
        }

        @Nullable
        @Override
        public CommandFunction<ServerCommandSource> getCommand(CommandFunctionManager funMan){
            return funMan.getFunction(functionId()).orElse(null);
        }

        @Override
        public int getItemDamage(){
            return itemDamage();
        }
    }

    record DataCommandLuckyFunction(String command, Identifier id, int rarityWeight, LuckyModifier modifier, Integer itemDamage) implements DataLuckyFunction{

        @Override
        public int getWeight(double luck){
            return modifier().modify(rarityWeight(), luck);
        }

        @Nullable
        @Override
        public CommandFunction<ServerCommandSource> getCommand(CommandFunctionManager funMan){
            Identifier madeUpID = id.withPath(p -> p + "_internal_synthesized_lucky_inline_function" + UUID.randomUUID());
            return CommandFunction.create(madeUpID,
                funMan.getDispatcher(),
                funMan.getScheduledCommandSource(),
                List.of(command())
            );
        }

        @Override
        public int getItemDamage(){
            return itemDamage();
        }
    }
}
