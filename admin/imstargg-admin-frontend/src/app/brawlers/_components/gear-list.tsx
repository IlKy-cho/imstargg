"use client";

import { Button } from "@/components/ui/button"
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import {
  Table,
  TableBody,
  TableCell,
  TableFooter,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { useEffect, useState } from "react"
import getGearList from "@/lib/api/getGearList"
import {Gear} from "@/model/Gear"

export function GearList() {
  const [gears, setGears] = useState<Gear[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchGears = async () => {
      try {
        const data = await getGearList();
        setGears(data);
      } catch (err) {
        setError("기어 목록을 불러오는데 실패했습니다.");
        console.error(err);
      } finally {
        setIsLoading(false);
      }
    };

    fetchGears();
  }, []);

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="outline">기어 목록</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[800px] h-[80vh]">
        <DialogHeader>
          <DialogTitle>기어 목록</DialogTitle>
        </DialogHeader>
        <div className="py-4 overflow-y-auto max-h-[calc(80vh-8rem)]">
          {isLoading && <p>로딩중...</p>}
          {error && <p className="text-red-500">{error}</p>}
          {!isLoading && !error && (
            <Table>
              <TableHeader className="sticky top-0 bg-white">
                <TableRow>
                  <TableHead>ID</TableHead>
                  <TableHead>이름</TableHead>
                  <TableHead className="text-right">메뉴</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {gears.map((gear) => (
                  <TableRow key={gear.entity.brawlStarsId}>
                    <TableCell>{gear.entity.rarity}</TableCell>
                    <TableCell>
                      {gear.names
                        .sort((a, b) => a.lang.localeCompare(b.lang))
                        .map(message => `${message.lang}(${message.content})`)
                        .join(' ')}
                    </TableCell>
                    <TableCell className="text-right">
                      메뉴
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
              <TableFooter className="sticky bottom-0 bg-white">
                <TableRow>
                  <TableCell colSpan={2}>Total</TableCell>
                  <TableCell className="text-right">{gears.length}</TableCell>
                </TableRow>
              </TableFooter>
            </Table>
          )}
        </div>
      </DialogContent>
    </Dialog>
  );
} 