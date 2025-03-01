import { getBrawlers } from "@/lib/api/brawler";
import { messagesContent } from "@/lib/message";
import { imageUrl } from "@/lib/image";
import Image from "next/image";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Table, TableBody, TableCell, TableHead, TableRow } from "@/components/ui/table";

type Props = {
  params: Promise<{ id: number }>
}

export default async function BrawlerDetailPage({ params }: Readonly<Props>) {
  const { id } = await params;
  const brawlers = await getBrawlers();  
  const brawler = brawlers.find(b => b.entity.brawlStarsId == id);

  if (!brawler) {
    return <div>브롤러를 찾을 수 없습니다.</div>;
  }

  return (
    <div className="space-y-6">
      <Card>
        <CardHeader>
          <CardTitle>브롤러 정보</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="flex gap-6">
            <div className="w-[200px]">
              {brawler.image ? (
                <Image
                  src={imageUrl(brawler.image)}
                  alt={`${messagesContent(brawler.names)} 이미지`}
                  width={200}
                  height={200}
                  className="rounded-lg"
                />
              ) : (
                <div className="w-[200px] h-[200px] bg-gray-100 rounded-lg flex items-center justify-center">
                  <span className="text-2xl text-gray-400">이미지 없음</span>
                </div>
              )}
            </div>
            <div className="flex-1">
              <Table>
                <TableBody>
                  <TableRow>
                    <TableHead className="w-[200px]">이름</TableHead>
                    <TableCell>{messagesContent(brawler.names)}</TableCell>
                  </TableRow>
                  <TableRow>
                    <TableHead>희귀도</TableHead>
                    <TableCell>{brawler.entity.rarity}</TableCell>
                  </TableRow>
                  <TableRow>
                    <TableHead>분류</TableHead>
                    <TableCell>{brawler.entity.role}</TableCell>
                  </TableRow>
                </TableBody>
              </Table>
            </div>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>가젯</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-2 gap-4">
            {brawler.gadgets.map(gadget => (
              <Card key={gadget.entity.brawlStarsId}>
                <CardContent className="pt-6">
                  <div className="flex gap-4">
                    {gadget.image ? (
                      <Image
                        src={imageUrl(gadget.image)}
                        alt={`${messagesContent(gadget.names)} 이미지`}
                        width={100}
                        height={100}
                        className="rounded-lg"
                      />
                    ) : (
                      <div className="w-[100px] h-[100px] bg-gray-100 rounded-lg flex items-center justify-center">
                        <span className="text-xl text-gray-400">이미지 없음</span>
                      </div>
                    )}
                    <div>
                      <h3 className="font-bold">{messagesContent(gadget.names)}</h3>
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>스타 파워</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-2 gap-4">
            {brawler.starPowers.map(starPower => (
              <Card key={starPower.entity.brawlStarsId}>
                <CardContent className="pt-6">
                  <div className="flex gap-4">
                    {starPower.image ? (
                      <Image
                        src={imageUrl(starPower.image)}
                        alt={`${messagesContent(starPower.names)} 이미지`}
                        width={100}
                        height={100}
                        className="rounded-lg"
                      />
                    ) : (
                      <div className="w-[100px] h-[100px] bg-gray-100 rounded-lg flex items-center justify-center">
                        <span className="text-xl text-gray-400">이미지 없음</span>
                      </div>
                    )}
                    <div>
                      <h3 className="font-bold">{messagesContent(starPower.names)}</h3>
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>기어</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-2 gap-4">
            {brawler.gears.map(gear => (
              <Card key={gear.entity.brawlStarsId}>
                <CardContent className="pt-6">
                  <div className="flex gap-4">
                    {gear.image ? (
                      <Image
                        src={imageUrl(gear.image)}
                        alt={`${messagesContent(gear.names)} 이미지`}
                        width={100}
                        height={100}
                        className="rounded-lg"
                      />
                    ) : (
                      <div className="w-[100px] h-[100px] bg-gray-100 rounded-lg flex items-center justify-center">
                        <span className="text-xl text-gray-400">이미지 없음</span>
                      </div>
                    )}
                    <div>
                      <h3 className="font-bold">{messagesContent(gear.names)}</h3>
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  );
}