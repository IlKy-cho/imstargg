"use client";

import NotRegisteredBattleEvent from "@/model/NotRegisteredBattleEvent";
import BattleMap from "@/model/BattleMap";

import { Button } from "@/components/ui/button"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import {useEffect, useState} from "react"
import registerEvent from "@/lib/api/registerEvent"
import Image from "next/image"

type Props = {
  event: NotRegisteredBattleEvent;
  battleMaps: BattleMap[];
}

export default function EventAdd({event, battleMaps}: Props) {
  const [searchTerm, setSearchTerm] = useState(event.map);
  const [searchedMaps, setSearchedMaps] = useState<BattleMap[]>([]);
  const [selectedMap, setSelectedMap] = useState<BattleMap | null>(null);

  const handleSearch = () => {
    setSearchedMaps(battleMaps.filter(map =>
      map.names.some(name =>
        name.content.includes(searchTerm))
    ));
  }

  const handleSubmit = async () => {
    try {
      await registerEvent({
        brawlStarsId: event.brawlStarsId,
        mapCode: selectedMap!.entity.code
      });
      window.location.reload();
    } catch (error) {
      console.error("이벤트 등록 실패:", error);
    }
  };

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="outline">이벤트 추가</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>이벤트 추가</DialogTitle>
          <DialogDescription>
            eventId: {event.brawlStarsId}. map: {event.map}
          </DialogDescription>
        </DialogHeader>
        <div className="grid gap-4 py-4">
          <div className="grid grid-cols-4 items-center gap-4">
            <Input
              id="mapSearch"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="col-span-3"
              placeholder="맵 이름으로 검색..."
            />
            <Button onClick={handleSearch}>검색</Button>
          </div>

          <div className="grid gap-4 w-full">
            <div className="text-sm text-muted-foreground">
              검색 결과: {searchedMaps.length}개
            </div>

            {searchedMaps.map((map) => (
              <div
                key={map.entity.code}
                className={`p-4 border rounded-lg cursor-pointer w-full ${
                  selectedMap?.entity.code === map.entity.code
                    ? 'border-primary bg-primary/10'
                    : 'hover:bg-accent'
                }`}
                onClick={() => setSelectedMap(map)}
              >
                <div className="flex items-center gap-4 w-full">
                  <div className="relative w-24 h-24 flex-shrink-0 bg-muted rounded-md flex items-center justify-center">
                    {map.image ? (
                      <Image
                        src={map.image.url}
                        alt={map.names[0]?.content || '맵 이미지'}
                        fill
                        className="object-cover rounded-md"
                      />
                    ) : (
                      <span className="text-muted-foreground text-sm">이미지 없음</span>
                    )}
                  </div>
                  <div className="flex flex-col gap-2 flex-grow">
                    <div className="font-semibold">
                      {map.names
                      .sort((a, b) => a.lang.localeCompare(b.lang))
                      .map(name => `${name.lang}: ${name.content}`)
                      .join(' / ')}
                    </div>
                    <div className="text-sm text-muted-foreground">
                      코드: {map.entity.code}
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        <DialogFooter>
          <Button 
            type="submit" 
            onClick={handleSubmit}
            disabled={!selectedMap}
          >
            추가
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}