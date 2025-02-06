import {
  Table,
  TableBody,
  TableCell,
  TableFooter,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { imageUrl } from "@/lib/image";
import { messagesContent } from "@/lib/message";
import {Brawler} from "@/model/Brawler";
import Image from "next/image";
import {GadgetImageUpload} from "@/app/gadgets/_components/gadget-image-upload";

interface GadgetListProps {
  brawlers: Brawler[];
}

export default function GadgetList({brawlers}: GadgetListProps) {
  return (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead className="w-12">ID</TableHead>
          <TableHead className="w-64">이미지</TableHead>
          <TableHead>이름</TableHead>
          <TableHead>브롤러 이름</TableHead>
          <TableHead className="text-right">메뉴</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {brawlers.map(brawler => 
          brawler.gadgets.map(gadget => (
            <TableRow key={gadget.entity.brawlStarsId}>
              <TableCell>{gadget.entity.brawlStarsId}</TableCell>
              <TableCell>
                {gadget.image ? (
                  <Image
                    src={imageUrl(gadget.image)}
                    alt={gadget.entity.id + " 이미지"}
                    width={64}
                    height={64}
                  />
                ) : (
                  <div className="flex items-center justify-center w-16 h-16 bg-gray-100">
                    <span className="text-xl text-gray-400">X</span>
                  </div>
                )}
              </TableCell>
              <TableCell>{messagesContent(gadget.names)}</TableCell>
              <TableCell>{messagesContent(brawler.names)}</TableCell>
              <TableCell className="text-right">
                <GadgetImageUpload gadget={gadget} />
              </TableCell>
            </TableRow>
          ))
        )}
      </TableBody>
      <TableFooter>
        <TableRow>
          <TableCell colSpan={4}>Total</TableCell>
          <TableCell className="text-right">
            {brawlers.reduce((acc, brawler) => acc + brawler.gadgets.length, 0)}
          </TableCell>
        </TableRow>
      </TableFooter>
    </Table>
  )
}
