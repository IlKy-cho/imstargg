'use client';

import { Player } from "@/model/Player";
import { useRecentPlayers } from "@/hooks/useRecentPlayers";
import { useEffect } from "react";

export default function PlayerRecentTracker({ player }: { player: Player }) {
  const { addRecentPlayer } = useRecentPlayers();

  useEffect(() => {
    addRecentPlayer(player);
  }, [player]);

  return null;
}