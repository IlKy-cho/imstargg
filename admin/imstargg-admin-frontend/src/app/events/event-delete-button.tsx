"use client";

import {useState} from "react";
import {deleteEvent, restoreEvent} from "@/lib/api/event";
import {Button} from "@/components/ui/button";
import BattleEvent from "@/model/BattleEvent";

type Props = {
  battleEvent: BattleEvent;
}

export default function EventDeleteButton({battleEvent}: Readonly<Props>) {
  const [deleted, setDeleted] = useState<boolean>(battleEvent.entity.deleted);

  const handleDelete = !deleted
    ? async () => {
      await deleteEvent(battleEvent.entity.id);
      setDeleted(prev => !prev);
    }
    : async () => {
      await restoreEvent(battleEvent.entity.id);
      setDeleted(prev => !prev);
    }
  return (
    <Button
      className="text-xs"
      onClick={handleDelete}
    >
      {deleted ? "복구" : "삭제"}
    </Button>
  );
}