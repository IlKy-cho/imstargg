"use client";

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
import { useState } from "react"
import registerMap from "@/lib/api/registerMap"
import { Language, LanguageValues } from "@/model/enums/Language"

export function MapAdd() {
  const [names, setNames] = useState<Record<Language, string>>(
    Object.fromEntries(LanguageValues.map(lang => [lang, ""])) as Record<Language, string>
  )

  const handleSubmit = async () => {
    try {
      await registerMap({ names })
      window.location.reload();
    } catch (error) {
      console.error("맵 등록 실패:", error);
    }
  }

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="outline">맵 추가</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>맵 추가</DialogTitle>
          <DialogDescription>
            맵을 추가하기 전에 해당 맵이 존재하는지 확인해주세요.
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
          <Button type="submit" onClick={handleSubmit}>추가</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}
