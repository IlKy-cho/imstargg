"use client";

import BattleEvent from "@/model/BattleEvent";
import {Button} from "@/components/ui/button";
import {eventSeasoned, eventUnseasoned} from "@/lib/api/event";

type Props = {
  battleEvent: BattleEvent;
}

export default function EventSeasonButton({battleEvent}: Readonly<Props>) {

  const handleSeason = battleEvent.seasoned
    ? async () => eventSeasoned(battleEvent.entity.id)
    : async () => eventUnseasoned(battleEvent.entity.id);

  return (
    <Button
      className="text-xs text-gray-400"
      onClick={handleSeason}
    >
      {battleEvent.seasoned ? "시즌 취소" : "시즌 추가"}
    </Button>
  );
}