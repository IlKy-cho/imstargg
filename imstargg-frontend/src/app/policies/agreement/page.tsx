import { policyItems } from "@/config/docs";
import Link from "next/link";

export default async function AgreementsPage() {
  const supercellFanContentPolicyUrl = "https://www.supercell.com/fan-content-policy";
  return (
    <div className="space-y-4">
      <h1 className="text-2xl font-bold">{policyItems.agreement.label}</h1>
      <div>
        <h2 className="text-xl font-bold">아임스타지지 소개</h2>
        <p>아임스타지지(ImStargg)는 브롤스타즈 통계 사이트입니다.</p>
        <p>브롤스타즈 전적기록을 확인할 수 있고, 브롤러 승률, 조합, 카운터 등 통계 정보를 제공합니다.</p>
      </div>
      <div>
        <h2 className="text-xl font-bold">슈퍼셀 Fan Content Policy</h2>
        <div>
          <p>This content is not affiliated with, endorsed, sponsored, or specifically approved by Supercell and Supercell is not responsible for it.</p>
          <p> For more information see Supercell&apos;s Fan Content Policy(<Link href={supercellFanContentPolicyUrl}>{supercellFanContentPolicyUrl}</Link>).</p>
        </div>
      </div>
    </div>
  );
}