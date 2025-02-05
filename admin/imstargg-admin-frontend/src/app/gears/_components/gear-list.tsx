import { Gear } from "@/model/Gear";

import {
  Table,
  TableBody,
  TableCell,
  TableFooter,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { messagesContent } from "@/lib/message";

export async function GearList({gears}: Readonly<{gears: Gear[]}>) {
  return (
    <Table>
      <TableHeader className="bg-white">
        <TableRow>
          <TableHead>ID</TableHead>
          <TableHead>희귀도</TableHead>
          <TableHead>이름</TableHead>
          <TableHead className="text-right">메뉴</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {gears.map((gear) => (
          <TableRow key={gear.entity.brawlStarsId}>
            <TableCell>{gear.entity.brawlStarsId}</TableCell>
            <TableCell>{gear.entity.rarity}</TableCell>
            <TableCell>
              {messagesContent(gear.names)}
            </TableCell>
            <TableCell className="text-right">

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
  );
}