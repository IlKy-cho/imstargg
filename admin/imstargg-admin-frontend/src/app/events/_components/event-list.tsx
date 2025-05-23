import {Table, TableBody, TableCell, TableFooter, TableHead, TableHeader, TableRow,} from "@/components/ui/table"
import Image from "next/image"
import {BattleEvent} from "@/model/BattleEvent";
import React from "react";
import {EventMapImageUpload} from "./event-map-image-upload";
import {imageUrl} from "@/lib/image";
import {messagesContent} from "@/lib/message";
import {EventUpdate} from "./event-update";

type Props = {
  battleEvents: BattleEvent[];
}

export default function EventList({battleEvents}: Readonly<Props>) {
  console.log(battleEvents);
  return (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead className="w-12">ID</TableHead>
          <TableHead className="w-[100px]">이미지</TableHead>
          <TableHead>이벤트 모드</TableHead>
          <TableHead>배틀 모드</TableHead>
          <TableHead>맵 이름</TableHead>
          <TableHead>경쟁전</TableHead>
          <TableHead>최근 전투 일시</TableHead>
          <TableHead className="text-right">메뉴</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {
          battleEvents
            .sort((a, b) => (a.latestBattleTime?.getTime() || 0) - (b.latestBattleTime?.getTime() || 0))
            .reverse()
            .map((battleEvent) => (
              <TableRow key={battleEvent.entity.brawlStarsId}>
                <TableCell>
                  {battleEvent.entity.brawlStarsId}
                </TableCell>
                <TableCell>
                  {battleEvent.map.image ? (
                    <Image
                      src={imageUrl(battleEvent.map.image)}
                      alt={battleEvent.entity.brawlStarsId + " 이벤트 이미지"}
                      width={100}
                      height={100}
                    />
                  ) : (
                    <div className="flex items-center justify-center w-[100px] h-[100px] bg-gray-100">
                      <span className="text-2xl text-gray-400">X</span>
                    </div>
                  )}
                </TableCell>
                <TableCell>
                  {battleEvent.entity.mode}
                </TableCell>
                <TableCell>
                  {battleEvent.battleMode}
                </TableCell>
                <TableCell>
                  <span className="font-bold">{battleEvent.entity.mapBrawlStarsName}</span>: {messagesContent(battleEvent.map.names)}
                </TableCell>
                <TableCell>
                  {battleEvent.soloRanked && "O"}
                </TableCell>
                <TableCell>
                  {battleEvent.latestBattleTime !== null
                    ? battleEvent.latestBattleTime.toLocaleString()
                    : "X"
                  }
                </TableCell>
                <TableCell className="flex flex-col">
                    {battleEvent.entity.brawlStarsId !== null && battleEvent.entity.brawlStarsId !== 0 ?
                      <EventMapImageUpload battleEvent={battleEvent}/>
                      : null
                    }
                    {battleEvent.entity.brawlStarsId !== null && battleEvent.entity.brawlStarsId !== 0 ?
                      <EventUpdate battleEvent={battleEvent}/>
                      : null
                    }
                </TableCell>
              </TableRow>
            ))
        }
      </TableBody>
      <TableFooter>
        <TableRow>
          <TableCell colSpan={3}>Total</TableCell>
          <TableCell className="text-right">{battleEvents.length}</TableCell>
        </TableRow>
      </TableFooter>
    </Table>
  );
}