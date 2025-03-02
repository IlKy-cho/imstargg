import { Player } from "@/model/Player";
import { useState, useEffect } from "react";

const STORAGE_KEY = "recent-players";
const MAX_ITEMS = 10;

type RecentPlayerItem = { name: string; tag: string };

export const useRecentPlayers = () => {
  const [recentPlayers, setRecentPlayers] = useState<RecentPlayerItem[]>([]);

  useEffect(() => {
    if (typeof window !== "undefined") {
      const storedPlayers = localStorage.getItem(STORAGE_KEY);
      if (storedPlayers) {
        setRecentPlayers(JSON.parse(storedPlayers));
      }
    }
  }, []);

  const addRecentPlayer = (item: Player) => {
    if (typeof window === "undefined") return;

    let updatedRecentPlayerItems: RecentPlayerItem[];

    updatedRecentPlayerItems = [
      { name: item.name, tag: item.tag },
      ...recentPlayers.filter(
        (t) => !(t.name === item.name && t.tag === item.tag)
      )
    ];

    if (updatedRecentPlayerItems.length > MAX_ITEMS) {
      updatedRecentPlayerItems = updatedRecentPlayerItems.slice(0, MAX_ITEMS);
    }

    setRecentPlayers(updatedRecentPlayerItems);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(updatedRecentPlayerItems));
  };

  const removeRecentPlayer = (item: RecentPlayerItem) => {
    if (typeof window === "undefined") return;

    let updatedRecentPlayerItems: RecentPlayerItem[];

    updatedRecentPlayerItems = recentPlayers.filter(
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