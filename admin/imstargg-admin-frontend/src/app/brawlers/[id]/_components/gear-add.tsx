"use client";

import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { useState, useEffect } from "react";
import { getGears, registerBrawlerGear } from "@/lib/api/brawler";
import { Gear } from "@/model/Gear";
import { Checkbox } from "@/components/ui/checkbox";
import { Label } from "@/components/ui/label";
import { messagesContent } from "@/lib/message";

export function GearAdd({ brawlStarsId }: { brawlStarsId: number }) {
  const [gears, setGears] = useState<Gear[]>([]);
  const [selectedGearIds, setSelectedGearIds] = useState<number[]>([]);

  useEffect(() => {
    getGears()
      .then(setGears)
      .catch(error => console.error("기어 목록 로딩 실패:", error));
  }, []);

  const handleSubmit = async () => {
    try {
      for (const gearId of selectedGearIds) {
        await registerBrawlerGear(brawlStarsId, { brawlStarsId: gearId });
      }
      window.location.reload();
    } catch (error) {
      console.error("기어 등록 실패:", error);
    }
  };

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="outline">기어 추가</Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>기어 추가</DialogTitle>
          <DialogDescription>
            새로운 기어를 추가합니다.
          </DialogDescription>
        </DialogHeader>
        <div className="grid gap-4 py-4">
          {gears.map((gear) => (
            <div key={gear.entity.brawlStarsId} className="flex items-center space-x-2">
              <Checkbox
                id={`gear-${gear.entity.brawlStarsId}`}
                checked={selectedGearIds.includes(gear.entity.brawlStarsId)}
                onCheckedChange={(checked) => {
                  if (checked) {
                    setSelectedGearIds([...selectedGearIds, gear.entity.brawlStarsId]);
                  } else {
                    setSelectedGearIds(selectedGearIds.filter(id => id !== gear.entity.brawlStarsId));
                  }
                }}
              />
              <Label htmlFor={`gear-${gear.entity.brawlStarsId}`}>
                {messagesContent(gear.names)}
              </Label>
            </div>
          ))}
        </div>
        <DialogFooter>
          <Button onClick={handleSubmit}>추가</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
} 