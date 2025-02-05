"use client";

import {BattleEvent} from "@/model/BattleEvent";
import {Button} from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import {Input} from "@/components/ui/input";
import {Label} from "@/components/ui/label";
import {useState} from "react";
import {Language, LanguageValues} from "@/model/enums/Language";
import {updateEvent} from "@/lib/api/event";

interface EventUpdateProps {
  battleEvent: BattleEvent;
}

export function EventUpdate({ battleEvent }: EventUpdateProps) {
  // 초기값 설정: battleEvent.map.names에서 language를 key로 하는 Record 생성
  const initialNames = Object.fromEntries(
    LanguageValues.map(lang => [
      lang,
      battleEvent.map.names.find(n => n.lang === lang)?.content || ""
    ])
  ) as Record<Language, string>;

  const [names, setNames] = useState<Record<Language, string>>(initialNames);

  const handleSubmit = async () => {
    if (!battleEvent.entity.brawlStarsId) {
      throw new Error("이벤트의 Brawl Stars ID가 없습니다.");
    }

    try {
      await updateEvent(battleEvent.entity.brawlStarsId, { names });
      window.location.reload();
    } catch (error) {
      console.error("이벤트 업데이트 실패:", error);
    }
  };

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="outline" size="sm">이벤트 수정</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[625px]">
        <DialogHeader>
          <DialogTitle>이벤트 수정</DialogTitle>
          <DialogDescription>
            이벤트 정보를 수정합니다.
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