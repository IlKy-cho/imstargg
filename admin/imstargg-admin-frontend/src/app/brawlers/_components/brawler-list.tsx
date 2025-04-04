import Image from 'next/image';
import Link from 'next/link';
import {Table, TableBody, TableCell, TableFooter, TableHead, TableHeader, TableRow,} from "@/components/ui/table"
import {Button} from "@/components/ui/button"
import {Popover, PopoverContent, PopoverTrigger,} from "@/components/ui/popover"
import {BrawlerImageUpload} from './brawler-image-upload';
import {imageUrl} from "@/lib/image";
import {messagesContent} from "@/lib/message";
import {Brawler} from "@/model/Brawler";

export async function BrawlerList({brawlers}: Readonly<{ brawlers: Brawler[] }>) {

  return (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead className="w-[100px]">이미지</TableHead>
          <TableHead>이름</TableHead>
          <TableHead>희귀도</TableHead>
          <TableHead>분류</TableHead>
          <TableHead>가젯</TableHead>
          <TableHead>기어</TableHead>
          <TableHead>스타파워</TableHead>
          <TableHead className="text-right">메뉴</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {brawlers.map((brawler) => (
          <TableRow key={brawler.entity.brawlStarsId}>
            <TableCell>
              <Link href={`/brawlers/${brawler.entity.brawlStarsId}`}>
                {brawler.image ? (
                  <Image
                    src={imageUrl(brawler.image)}
                    alt={brawler.entity.id + " 이미지"}
                    width={100}
                    height={100}
                    className="cursor-pointer hover:opacity-80 transition-opacity"
                  />
                ) : (
                  <div className="flex items-center justify-center w-[100px] h-[100px] bg-gray-100 cursor-pointer hover:bg-gray-200 transition-colors">
                    <span className="text-2xl text-gray-400">X</span>
                  </div>
                )}
              </Link>
            </TableCell>
            <TableCell>
              {messagesContent(brawler.names)}
            </TableCell>
            <TableCell>{brawler.entity.rarity}</TableCell>
            <TableCell>{brawler.entity.role}</TableCell>
            <TableCell>
              <Popover>
                <PopoverTrigger asChild>
                  <Button variant="outline">{brawler.gadgets.length}</Button>
                </PopoverTrigger>
                <PopoverContent className="max-w-[300px]">
                  {
                    brawler.gadgets.map((gadget, index, array) => (
                      <div 
                        key={gadget.entity.brawlStarsId} 
                        className={`items-center block py-2 ${index !== array.length - 1 ? 'border-b' : ''}`}
                      >
                        {messagesContent(gadget.names)}
                      </div>
                    ))
                  }
                </PopoverContent>
              </Popover>
            </TableCell>
            <TableCell>
              <Popover>
                <PopoverTrigger asChild>
                  <Button variant="outline">{brawler.gears.length}</Button>
                </PopoverTrigger>
                <PopoverContent className="max-w-[300px]">
                  {
                    brawler.gears.map((gear, index, array) => (
                      <div 
                        key={gear.entity.brawlStarsId} 
                        className={`items-center block py-2 ${index !== array.length - 1 ? 'border-b' : ''}`}
                      >
                        {messagesContent(gear.names)}
                      </div>
                    ))
                  }
                </PopoverContent>
              </Popover>
            </TableCell>
            <TableCell>
              <Popover>
                <PopoverTrigger asChild>
                  <Button variant="outline">{brawler.starPowers.length}</Button>
                </PopoverTrigger>
                <PopoverContent className="max-w-[300px]">
                  {
                    brawler.starPowers.map((starPower, index, array) => (
                      <div 
                        key={starPower.entity.brawlStarsId} 
                        className={`items-center block py-2 ${index !== array.length - 1 ? 'border-b' : ''}`}
                      >
                        {messagesContent(starPower.names)}
                      </div>
                    ))
                  }
                </PopoverContent>
              </Popover>
            </TableCell>
            <TableCell className="text-right space-x-2">
              <BrawlerImageUpload brawlStarsId={brawler.entity.brawlStarsId} />
            </TableCell>
          </TableRow>
        ))}
      </TableBody>
      <TableFooter>
        <TableRow>
          <TableCell colSpan={3}>Total</TableCell>
          <TableCell className="text-right">{brawlers.length}</TableCell>
        </TableRow>
      </TableFooter>
    </Table>
  )
}
