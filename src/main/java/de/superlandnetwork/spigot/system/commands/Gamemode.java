/*
 * MIT License
 *
 * Copyright (c) 2019 Filli Group (Einzelunternehmen)
 * Copyright (c) 2019 Filli IT (Einzelunternehmen)
 * Copyright (c) 2019 Filli Games (Einzelunternehmen)
 * Copyright (c) 2019 Ursin Filli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package de.superlandnetwork.spigot.system.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Gamemode implements CommandExecutor, TabCompleter {

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender Source of the command
     * @param cmd    Command which was executed
     * @param label  Alias of the command which was used
     * @param args   Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§7[§6System§7] §cJust for Player");
                return true;
            }

            Player p = (Player) sender;
            setGameMode(sender, args[0], p);
            return true;
        }

        if (args.length == 2) {
            if (!sender.hasPermission("system.command.gamemode.other")) {
                sender.sendMessage("§cYou not have permission to use this command.");
                return true;
            }

            Player t = Bukkit.getPlayer(args[1]);
            if (t == null) {
                sender.sendMessage("§7[§6System§7] §cPlayer not Found");
                return true;
            }

            setGameMode(sender, args[0], t);
            return true;
        }
        return false;
    }

    private void setGameMode(CommandSender sender, String s, Player t) {
        GameModeEnum gameModeEnum = null;
        gameModeEnum = GameModeEnum.getByID(s);

        if (gameModeEnum == null)
            gameModeEnum = GameModeEnum.getByName(s);

        if (gameModeEnum == null) {
            sender.sendMessage("§7[§6System§7] §cGamemode not Found!");
            return;
        }

        t.setGameMode(gameModeEnum.getGameMode());
        sender.sendMessage("§7[§6System§7] §eGamemode changed");
    }

    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender Source of the command.  For players tab-completing a
     *               command inside of a command block, this will be the player, not
     *               the command block.
     * @param cmd    Command which was executed
     * @param alias  The alias used
     * @param args   The arguments passed to the command, including final
     *               partial argument to be completed and command label
     * @return A List of possible completions for the final argument, or null
     * to default to the command executor
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            for (GameModeEnum e : GameModeEnum.values()) {
                list.add(e.getId());
                list.add(e.getName());
            }
        }

        if (args.length == 2) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                list.add(p.getName());
            }
        }
        return list;
    }

    public enum GameModeEnum {
        SURVIVAL("0", "survival", GameMode.SURVIVAL),
        CREATIVE("1", "creative", GameMode.CREATIVE),
        ADVENTURE("2", "adventure", GameMode.ADVENTURE),
        SPECTATOR("3", "spectator", GameMode.SPECTATOR);

        private String id, name;
        private GameMode gameMode;

        GameModeEnum(String id, String name, GameMode gameMode) {
            this.id = id;
            this.name = name;
            this.gameMode = gameMode;
        }

        public static GameModeEnum getByID(String id) {
            for (GameModeEnum e : GameModeEnum.values()) {
                if (e.getId().equals(id))
                    return e;
            }

            return null;
        }

        public static GameModeEnum getByName(String name) {
            for (GameModeEnum e : GameModeEnum.values()) {
                if (e.getName().equalsIgnoreCase(name))
                    return e;
            }

            return null;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public GameMode getGameMode() {
            return gameMode;
        }
    }

}
