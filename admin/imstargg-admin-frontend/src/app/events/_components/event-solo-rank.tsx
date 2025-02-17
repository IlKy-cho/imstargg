"use client";

import { Button } from "@/components/ui/button";
import { deleteSoloRankEvent, registerSoloRankEvent } from "@/lib/api/event";
import {BattleEvent} from "@/model/BattleEvent";
import {useRouter} from "next/navigation";

export function EventSoloRank({battleEvent}: { battleEvent: BattleEvent }) {
  if (battleEvent.soloRanked) {
    return <EventSoloRankDelete battleEvent={battleEvent}/>
  } else {
    return <EventSoloRankRegister battleEvent={battleEvent}/>
  }
}

function EventSoloRankRegister({battleEvent}: { battleEvent: BattleEvent }) {
  const router = useRouter();
  const handleRegister = async () => {
    await registerSoloRankEvent(battleEvent.entity.brawlStarsId!);
    router.refresh();
  }

  return (
    <Button variant="outline" size="sm" onClick={handleRegister}>
      솔로 랭크 등록
    </Button>
  ) 
}

function EventSoloRankDelete({battleEvent}: { battleEvent: BattleEvent }) {
  const router = useRouter();

  const handleDelete = async () => {
    await deleteSoloRankEvent(battleEvent.entity.brawlStarsId!);
    router.refresh();
  }

  return (
    <Button variant="outline" size="sm" onClick={handleDelete}>
      솔로 랭크 삭제
    </Button>
  )
}
