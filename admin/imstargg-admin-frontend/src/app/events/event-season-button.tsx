"use client";

import BattleEvent from "@/model/BattleEvent";
import {Button} from "@/components/ui/button";
import {eventSeasoned, eventUnseasoned} from "@/lib/api/event";
import {useState} from "react";

type Props = {
  battleEvent: BattleEvent;
}

export default function EventSeasonButton({battleEvent}: Readonly<Props>) {
  const [seasoned, setSeasoned] = useState(battleEvent.seasoned);
  const handleSeason = !seasoned
    ? async () => {
      await eventSeasoned(battleEvent.entity.id);
      setSeasoned(prev => !prev);
    }
    : async () => {
      await eventUnseasoned(battleEvent.entity.id);
      setSeasoned(prev => !prev);
    };

  return (
    <Button
      className="text-xs text-gray-400"
      onClick={handleSeason}
    >
      {seasoned ? "시즌 취소" : "시즌 추가"}
    </Button>
  );
}