import { Player } from "@/model/Player";
import { useState } from "react";

const STORAGE_KEY = "recent-players";
const MAX_ITEMS = 10;

type RecentPlayerItem = { name: string; tag: string; viewedAt: string };

export const useRecentPlayers = () => {
  const [recentPlayers, setRecentPlayers] = useState<RecentPlayerItem[]>(() => {
    if (typeof window !== "undefined") {
      const storedPlayers = localStorage.getItem(STORAGE_KEY);
      if (storedPlayers) {
        return JSON.parse(storedPlayers);
      }
    }
    return [];
  });

  const addRecentPlayer = (item: Player) => {
    if (typeof window === "undefined") return;

    setRecentPlayers(prevPlayers => {
      const updatedRecentPlayerItems = [
        { name: item.name, tag: item.tag, viewedAt: new Date().toISOString() },
        ...prevPlayers.filter(
          (t) => !(t.name === item.name && t.tag === item.tag)
        )
      ].slice(0, MAX_ITEMS);

      localStorage.setItem(STORAGE_KEY, JSON.stringify(updatedRecentPlayerItems));
      return updatedRecentPlayerItems;
    });
  };

  const removeRecentPlayer = (item: RecentPlayerItem) => {
    if (typeof window === "undefined") return;

    const updatedRecentPlayerItems = recentPlayers.filter(
      (t) => !(t.name === item.name && t.tag === item.tag)
    );

    setRecentPlayers(updatedRecentPlayerItems);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(updatedRecentPlayerItems));
  };

  const clearRecentPlayers = () => {
    if (typeof window === "undefined") return;
    setRecentPlayers([]);
    localStorage.removeItem(STORAGE_KEY);
  };

  return { recentPlayers, addRecentPlayer, removeRecentPlayer, clearRecentPlayers };
};