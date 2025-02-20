import {Metadata} from "next";
import {PageHeader, pageHeaderContainerDefault} from "@/components/page-header";
import {cn} from "@/lib/utils";
import {
  PageGroupedBattleEventList,
  PageRotationBattleEventList,
  PageSoloRankBattleEventList
} from "./_components/event-list";

export const metadata: Metadata = {
  title: `브롤스타즈 이벤트`,
  description: "브롤스타즈의 이벤트 목록입니다. 각 이벤트의 통계 정보를 확인할 수 있습니다.",
};

export default async function EventsPage() {

  return (
    <div className="space-y-2">
      <PageHeader>
        <div className={cn("flex flex-col gap-2", pageHeaderContainerDefault)}>
          <h1 className="sm:text-2xl text-xl font-bold text-zinc-800">
            이벤트
          </h1>
        </div>
      </PageHeader>
      <div className="p-1 flex flex-col gap-2">
        <PageRotationBattleEventList/>
        <PageSoloRankBattleEventList/>
        <PageGroupedBattleEventList/>
      </div>
    </div>
  );
}

