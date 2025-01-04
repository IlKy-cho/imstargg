import {Table, TableBody, TableCell, TableFooter, TableHead, TableHeader, TableRow,} from "@/components/ui/table"
import getNotRegisteredEventList from "@/lib/api/getNotRegisteredEventList";
import EventAdd from "./event-add";
import {getMapList} from "@/lib/api/event";

export async function NotRegisteredEventList() {
  const battleMaps = await getMapList();
  const eventList = await getNotRegisteredEventList();

  return (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead className="w-[100px]">아이디</TableHead>
          <TableHead>모드</TableHead>
          <TableHead>맵</TableHead>
          <TableHead>최근 전투일시</TableHead>
          <TableHead className="text-right">메뉴</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {eventList.map((event) => (
          <TableRow key={event.brawlStarsId}>
            <TableCell>{event.brawlStarsId}</TableCell>
            <TableCell>{event.mode}</TableCell>
            <TableCell>{event.map}</TableCell>
            <TableCell>{event.battleTime.toLocaleString()}</TableCell>
            <TableCell className="text-right space-x-2">
              <EventAdd event={event} battleMaps={battleMaps} />
            </TableCell>
          </TableRow>
        ))}
      </TableBody>
      <TableFooter>
        <TableRow>
          <TableCell colSpan={3}>Total</TableCell>
          <TableCell className="text-right">{battleMaps.length}</TableCell>
        </TableRow>
      </TableFooter>
    </Table>
  )
}
