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
import registerBrawler from "@/lib/api/registerBrawler"
import { LanguageType, LanguageValues } from "@/model/enums/Language"
import {BrawlerRole, BrawlerRoleType, BrawlerRoleValues} from "@/model/enums/BrawlerRole"
import {BrawlerRarity, BrawlerRarityType, BrawlerRarityValues} from "@/model/enums/BrawlerRarity";
import { XIcon } from "lucide-react"
import getGearList from "@/lib/api/getGearList"
import Gear from "@/model/Gear"

interface GadgetForm {
  brawlStarsId: string;
  names: Record<LanguageType, string>;
}

interface StarPowerForm {
  brawlStarsId: string;
  names: Record<LanguageType, string>;
}

export function BrawlerAdd() {
  const [brawlStarsId, setBrawlStarsId] = useState<string>("");
  const [rarity, setRarity] = useState<BrawlerRarityType>(BrawlerRarity.STARTING_BRAWLER);
  const [role, setRole] = useState<BrawlerRoleType>(BrawlerRole.DAMAGE_DEALER);
  const [names, setNames] = useState<Record<LanguageType, string>>(
    Object.fromEntries(LanguageValues.map(lang => [lang, ""])) as Record<LanguageType, string>
  );
  const [gadgets, setGadgets] = useState<GadgetForm[]>([]);
  const [starPowers, setStarPowers] = useState<StarPowerForm[]>([]);
  const [gearIds, setGearIds] = useState<number[]>([]);
  const [gears, setGears] = useState<Gear[]>([]);

  useEffect(() => {
    const fetchGears = async () => {
      try {
        const gearList = await getGearList();
        setGears(gearList);
      } catch (error) {
        console.error("기어 목록 로딩 실패:", error);
      }
    };
    
    fetchGears();
  }, []);

  const handleSubmit = async () => {
    try {
      await registerBrawler({
        brawlStarsId: Number(brawlStarsId),
        rarity,
        role,
        names,
        gearIds,
        gadgets: gadgets.map(g => ({
          brawlStarsId: Number(g.brawlStarsId),
          names: g.names
        })),
        starPowers: starPowers.map(s => ({
          brawlStarsId: Number(s.brawlStarsId),
          names: s.names
        }))
      });
      window.location.reload();
    } catch (error) {
      console.error("브롤러 등록 실패:", error);
    }
  };

  const addGadget = () => {
    setGadgets([...gadgets, {
      brawlStarsId: "",
      names: Object.fromEntries(LanguageValues.map(lang => [lang, ""])) as Record<LanguageType, string>
    }]);
  };

  const addStarPower = () => {
    setStarPowers([...starPowers, {
      brawlStarsId: "",
      names: Object.fromEntries(LanguageValues.map(lang => [lang, ""])) as Record<LanguageType, string>
    }]);
  };

  const removeGadget = (index: number) => {
    setGadgets(gadgets.filter((_, i) => i !== index));
  };

  const removeStarPower = (index: number) => {
    setStarPowers(starPowers.filter((_, i) => i !== index));
  };

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="outline">브롤러 추가</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[625px] h-[80vh] overflow-y-auto">
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
            <Select value={rarity} onValueChange={(value) => setRarity(value as BrawlerRarityType)}>
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
            <Select value={role} onValueChange={(value) => setRole(value as BrawlerRoleType)}>
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

          <div className="border-t pt-4">
            <div className="flex justify-between items-center mb-4">
              <h4 className="font-semibold">가젯 추가</h4>
              <Button type="button" variant="outline" onClick={addGadget}>가젯 추가</Button>
            </div>
            {gadgets.map((gadget, index) => (
              <div key={index} className="border p-4 mb-4 rounded-lg relative">
                <Button
                  variant="ghost"
                  size="icon"
                  className="absolute right-2 top-2"
                  onClick={() => removeGadget(index)}
                >
                  <XIcon className="h-4 w-4" />
                </Button>
                <div className="grid grid-cols-4 items-center gap-4 mb-4">
                  <Label className="text-right">브롤스타즈 ID</Label>
                  <Input
                    type="number"
                    value={gadget.brawlStarsId}
                    onChange={(e) => {
                      const newGadgets = [...gadgets];
                      newGadgets[index].brawlStarsId = e.target.value;
                      setGadgets(newGadgets);
                    }}
                    className="col-span-3"
                  />
                </div>
                {LanguageValues.map((language) => (
                  <div key={language} className="grid grid-cols-4 items-center gap-4">
                    <Label className="text-right">이름({language})</Label>
                    <Input
                      value={gadget.names[language]}
                      onChange={(e) => {
                        const newGadgets = [...gadgets];
                        newGadgets[index].names[language] = e.target.value;
                        setGadgets(newGadgets);
                      }}
                      className="col-span-3"
                    />
                  </div>
                ))}
              </div>
            ))}
          </div>

          <div className="border-t pt-4">
            <div className="flex justify-between items-center mb-4">
              <h4 className="font-semibold">스타파워</h4>
              <Button type="button" variant="outline" onClick={addStarPower}>스타파워 추가</Button>
            </div>
            {starPowers.map((starPower, index) => (
              <div key={index} className="border p-4 mb-4 rounded-lg relative">
                <Button
                  variant="ghost"
                  size="icon"
                  className="absolute right-2 top-2"
                  onClick={() => removeStarPower(index)}
                >
                  <XIcon className="h-4 w-4" />
                </Button>
                <div className="grid grid-cols-4 items-center gap-4 mb-4">
                  <Label className="text-right">브롤스타즈 ID</Label>
                  <Input
                    type="number"
                    value={starPower.brawlStarsId}
                    onChange={(e) => {
                      const newStarPowers = [...starPowers];
                      newStarPowers[index].brawlStarsId = e.target.value;
                      setStarPowers(newStarPowers);
                    }}
                    className="col-span-3"
                  />
                </div>
                {LanguageValues.map((language) => (
                  <div key={language} className="grid grid-cols-4 items-center gap-4">
                    <Label className="text-right">이름({language})</Label>
                    <Input
                      value={starPower.names[language]}
                      onChange={(e) => {
                        const newStarPowers = [...starPowers];
                        newStarPowers[index].names[language] = e.target.value;
                        setStarPowers(newStarPowers);
                      }}
                      className="col-span-3"
                    />
                  </div>
                ))}
              </div>
            ))}
          </div>

          <div className="border-t pt-4">
            <div className="flex justify-between items-center mb-4">
              <h4 className="font-semibold">기어</h4>
            </div>
            <div className="grid grid-cols-4 gap-4">
              {gears.map((gear) => (
                <div key={gear.entity.id} className="flex items-center gap-2">
                  <input
                    type="checkbox"
                    id={`gear-${gear.entity.id}`}
                    checked={gearIds.includes(gear.entity.id)}
                    onChange={(e) => {
                      if (e.target.checked) {
                        setGearIds([...gearIds, gear.entity.id]);
                      } else {
                        setGearIds(gearIds.filter(id => id !== gear.entity.id));
                      }
                    }}
                  />
                  <Label htmlFor={`gear-${gear.entity.id}`}>
                    {gear.names.sort((a, b) => a.lang.localeCompare(b.lang)).map(message => `${message.lang}(${message.content})`).join(' ')}
                  </Label>
                </div>
              ))}
            </div>
          </div>
        </div>
        <DialogFooter>
          <Button type="submit" onClick={handleSubmit}>추가</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
} 