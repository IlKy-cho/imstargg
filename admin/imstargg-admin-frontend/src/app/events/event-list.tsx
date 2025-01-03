import {
  Table,
  TableBody,
  TableCell,
  TableFooter,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import Image from "next/image"
import BattleEvent from "@/model/BattleEvent";
import {messagesToTitle} from "@/components/title";
import React from "react";
import EventSeasonButton from "@/app/events/event-season-button";

type Props = {
  battleEvents: BattleEvent[];
}

export default function EventList({battleEvents}: Readonly<Props>) {
  return (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead className="w-12">ID</TableHead>
          <TableHead className="w-[100px]">이미지</TableHead>
          <TableHead>모드</TableHead>
          <TableHead>맵 이름</TableHead>
          <TableHead>시즌</TableHead>
          <TableHead className="text-right">메뉴</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {
          battleEvents
            .sort((a, b) => a.entity.brawlStarsId - b.entity.brawlStarsId)
            .map((battleEvent) => (
            <TableRow key={battleEvent.entity.id}>
              <TableCell>
                {battleEvent.entity.brawlStarsId}
              </TableCell>
              <TableCell>
                {battleEvent.map.image ? (
                  <Image
                    src={battleEvent.map.image.url}
                    alt={battleEvent.map.entity.id + " 이미지"}
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
                {messagesToTitle(battleEvent.map.names)}
              </TableCell>
              <TableCell>
                {battleEvent.seasoned ? "O" : null}
              </TableCell>
              <TableCell>
                <EventSeasonButton battleEvent={battleEvent} />
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