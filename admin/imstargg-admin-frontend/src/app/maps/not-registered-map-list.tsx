"use client";

import {Button} from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import getNotRegisteredEventList from "@/lib/api/getNotRegisteredEventList";
import BattleMap from "@/model/BattleMap";
import {useEffect, useState} from "react";

type Props = {
  battleMaps: BattleMap[];
}
  
export function NotRegisteredMapList({ battleMaps }: Props) {
  const [notRegisteredMapNames, setNotRegisteredMapNames] = useState<string[]>([]);

  useEffect(() => {
    const fetchNotRegisteredMaps = async () => {
      const notRegisteredEventList = await getNotRegisteredEventList();
      const battleMapNames = new Set(battleMaps.map(map => map.names[0].content));
      
      const uniqueMapNames = new Set(
        notRegisteredEventList
          .map(event => event.map)
          .filter(mapName => !battleMapNames.has(mapName))
      );
      
      setNotRegisteredMapNames(Array.from(uniqueMapNames));
    };

    fetchNotRegisteredMaps();
  }, [battleMaps]);

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="outline">미등록 맵 확인</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px] max-h-[80vh]">
        <DialogHeader>
          <DialogTitle>미등록 맵 목록</DialogTitle>
          <DialogDescription>
            아직 등록되지 않은 맵 목록입니다.
          </DialogDescription>
        </DialogHeader>
        <div className="space-y-2 overflow-y-auto max-h-[60vh] pr-2">
          {notRegisteredMapNames.map((mapName) => (
            <div key={mapName} className="p-2 border rounded">
              {mapName}
            </div>
          ))}
        </div>
      </DialogContent>
    </Dialog>
  );
}