import Image from 'next/image';
import {Table, TableBody, TableCell, TableFooter, TableHead, TableHeader, TableRow,} from "@/components/ui/table"
import {imageUrl} from "@/lib/image";
import {messagesContent} from "@/lib/message";
import {Brawler} from "@/model/Brawler";
import { StarPowerImageUpload } from './starpower-image-upload';

export async function StarPowerList({brawlers}: Readonly<{ brawlers: Brawler[] }>) {
  return (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead>ID</TableHead>
          <TableHead className="w-64">이미지</TableHead>
          <TableHead>이름</TableHead>
          <TableHead>브롤러</TableHead>
          <TableHead className="text-right">메뉴</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {brawlers.map(brawler => 
          brawler.starPowers.map(starPower => (
            <TableRow key={starPower.entity.brawlStarsId}>
              <TableCell>{starPower.entity.brawlStarsId}</TableCell>
              <TableCell>
                {starPower.image ? (
                  <Image
                    src={imageUrl(starPower.image)}
                    alt={starPower.entity.id + " 이미지"}
                    width={64}
                    height={64}
                  />
                ) : (
                  <div className="flex items-center justify-center w-16 h-16 bg-gray-100">
                    <span className="text-xl text-gray-400">X</span>
                  </div>
                )}
              </TableCell>
              <TableCell>{messagesContent(starPower.names)}</TableCell>
              <TableCell>{messagesContent(brawler.names)}</TableCell>
              <TableCell className="text-right">
                <StarPowerImageUpload starPower={starPower} />
              </TableCell>
            </TableRow>
          ))
        )}
      </TableBody>
      <TableFooter>
        <TableRow>
          <TableCell colSpan={4}>Total</TableCell>
          <TableCell className="text-right">
            {brawlers.reduce((acc, brawler) => acc + brawler.starPowers.length, 0)}
          </TableCell>
        </TableRow>
      </TableFooter>
    </Table>
  );
} 