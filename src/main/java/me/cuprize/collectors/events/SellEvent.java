package me.cuprize.collectors.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SellEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final double moneyAmount;
    private final int itemsAmount;

    public SellEvent(Player player, double moneyAmount, int itemsAmount) {
        this.player = player;
        this.moneyAmount = moneyAmount;
        this.itemsAmount = itemsAmount;
    }

    public double getMoneyAmount() {
        return moneyAmount;
    }

    public int getItemsAmount() {
        return itemsAmount;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
