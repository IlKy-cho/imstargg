import Image from 'next/image';
import {Table, TableBody, TableCell, TableFooter, TableHead, TableHeader, TableRow,} from "@/components/ui/table"
import {MapImageUpload} from './map-image-upload';
import BattleMap from '@/model/BattleMap';

type Props = {
  battleMaps: BattleMap[];
}

export async function MapList({ battleMaps }: Props) {

  return (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead className="w-[100px]">이미지</TableHead>
          <TableHead>이름</TableHead>
          <TableHead>이벤트</TableHead>
          <TableHead className="text-right">메뉴</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {battleMaps.map((battleMap) => (
          <TableRow key={battleMap.entity.code}>
            <TableCell>
              {battleMap.image ? (
                <Image
                  src={battleMap.image.url}
                  alt={battleMap.entity.code + " 이미지"}
                  width={100}
                  height={100}
                />
              ) : (
                <div className="flex items-center justify-center w-[100px] h-[100px] bg-gray-100">
                  <span className="text-2xl text-gray-400">X</span>
                </div>
              )}
            </TableCell>
            <TableCell>
              {battleMap.names
                .sort((a, b) => a.lang.localeCompare(b.lang))
                .map(message => `${message.lang}(${message.content})`)
                .join(' ')}
            </TableCell>
            <TableCell>
              {battleMap.events
                .map(battleEvent => `${battleEvent.mode}(${battleEvent.brawlStarsId})`)
                .join(' ')}
            </TableCell>
            <TableCell className="text-right space-x-2">
              <MapImageUpload mapCode={battleMap.entity.code} />
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
