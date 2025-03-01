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
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useState } from "react";
import { Language, LanguageValues } from "@/model/enums/Language";
import { registerStarPower } from "@/lib/api/brawler";
import { initialNewMessages } from "@/lib/message";

export function StarPowerAdd({ brawlStarsId }: { brawlStarsId: number }) {
  const [starPowerBrawlStarsId, setStarPowerBrawlStarsId] = useState<string>("");
  const [names, setNames] = useState<Record<Language, string>>(initialNewMessages);

  const handleSubmit = async () => {
    try {
      await registerStarPower(brawlStarsId, {
        brawlStarsId: Number(starPowerBrawlStarsId),
        names,
      });
      window.location.reload();
    } catch (error) {
      console.error("스타파워 등록 실패:", error);
    }
  };

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="outline">스타파워 추가</Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>스타파워 추가</DialogTitle>
          <DialogDescription>
            새로운 스타파워를 추가합니다.
          </DialogDescription>
        </DialogHeader>
        <div className="grid gap-4 py-4">
          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="starPowerBrawlStarsId" className="text-right">
              브롤스타즈 ID
            </Label>
            <Input
              id="starPowerBrawlStarsId"
              type="number"
              value={starPowerBrawlStarsId}
              onChange={(e) => setStarPowerBrawlStarsId(e.target.value)}
              className="col-span-3"
            />
          </div>

          {LanguageValues.map((language) => (
            <div key={language} className="grid grid-cols-4 items-center gap-4">
              <Label htmlFor={`name-${language}`} className="text-right">
                이름({language})
              </Label>
              <Input
                id={`name-${language}`}
                value={names[language]}
                onChange={(e) => setNames({ ...names, [language]: e.target.value })}
                className="col-span-3"
              />
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