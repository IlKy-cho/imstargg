"use client";

import { Gear } from "@/model/Gear";
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
import { updateGear } from "@/lib/api/brawler";

interface GearUpdateProps {
  gear: Gear;
}

export function GearUpdate({ gear }: GearUpdateProps) {
  const initialNames = Object.fromEntries(
    LanguageValues.map(lang => [
      lang,
      gear.names.find(n => n.lang === lang)?.content || ""
    ])
  ) as Record<Language, string>;

  const [names, setNames] = useState<Record<Language, string>>(initialNames);

  const handleSubmit = async () => {
    try {
      await updateGear(gear.entity.brawlStarsId, { names });
      window.location.reload();
    } catch (error) {
      console.error("기어 업데이트 실패:", error);
    }
  };

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="outline" size="sm">기어 수정</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[625px]">
        <DialogHeader>
          <DialogTitle>기어 수정</DialogTitle>
          <DialogDescription>
            기어 정보를 수정합니다.
          </DialogDescription>
        </DialogHeader>
        <div className="grid gap-4 py-4">
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
          <Button type="submit" onClick={handleSubmit}>수정</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
} 