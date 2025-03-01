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
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import { useState, useEffect } from "react"
import { XIcon } from "lucide-react"
import {Gear} from "@/model/Gear"
import {Language, LanguageValues} from "@/model/enums/Language";
import {BrawlerRarity, BrawlerRarityValue, BrawlerRarityValues} from "@/model/enums/BrawlerRarity";
import {BrawlerRole, BrawlerRoleValue, BrawlerRoleValues} from "@/model/enums/BrawlerRole";
import {getGears, registerBrawler} from "@/lib/api/brawler";
import { initialNewMessages } from "@/lib/message";

interface GadgetForm {
  brawlStarsId: string;
  names: Record<Language, string>;
}

interface StarPowerForm {
  brawlStarsId: string;
  names: Record<Language, string>;
}

export function BrawlerAdd() {
  const [brawlStarsId, setBrawlStarsId] = useState<string>("");
  const [rarity, setRarity] = useState<BrawlerRarity>(BrawlerRarityValue.STARTING_BRAWLER);
  const [role, setRole] = useState<BrawlerRole>(BrawlerRoleValue.DAMAGE_DEALER);
  const [names, setNames] = useState<Record<Language, string>>(
    initialNewMessages
  );

  const handleSubmit = async () => {
    try {
      await registerBrawler({
        brawlStarsId: Number(brawlStarsId),
        rarity,
        role,
        names,
      });
      window.location.reload();
    } catch (error) {
      console.error("브롤러 등록 실패:", error);
    }
  };

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="outline">브롤러 추가</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[625px]">
        <DialogHeader>
          <DialogTitle>브롤러 추가</DialogTitle>
          <DialogDescription>
            브롤러를 추가하기 전에 해당 브롤러가 존재하는지 확인해주세요.
          </DialogDescription>
        </DialogHeader>
        <div className="grid gap-4 py-4">
          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="brawlStarsId" className="text-right">
              브롤스타즈 ID
            </Label>
            <Input
              id="brawlStarsId"
              type="number"
              value={brawlStarsId}
              onChange={(e) => setBrawlStarsId(e.target.value)}
              className="col-span-3"
            />
          </div>

          <div className="grid grid-cols-4 items-center gap-4">
            <Label className="text-right">희귀도</Label>
            <Select value={rarity} onValueChange={(value) => setRarity(value as BrawlerRarity)}>
              <SelectTrigger className="col-span-3">
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                {BrawlerRarityValues.map((r) => (
                  <SelectItem key={r} value={r}>{r}</SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className="grid grid-cols-4 items-center gap-4">
            <Label className="text-right">분류</Label>
            <Select value={role} onValueChange={(value) => setRole(value as BrawlerRole)}>
              <SelectTrigger className="col-span-3">
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                {BrawlerRoleValues.map((r) => (
                  <SelectItem key={r} value={r}>{r}</SelectItem>
                ))}
              </SelectContent>
            </Select>
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
          <Button type="submit" onClick={handleSubmit}>추가</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
} 