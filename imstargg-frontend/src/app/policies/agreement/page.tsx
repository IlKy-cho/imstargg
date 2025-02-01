import {policyItems} from "@/config/docs";

export default async function AgreementsPage() {
  return (
    <div className="space-y-4">
      <h1 className="text-2xl font-bold">{policyItems.agreement.label}</h1>
      <div>
        <h2 className="text-xl font-bold">아임스타지지 소개</h2>
        <p>아임스타지지(ImStargg)는 브롤스타즈 통계 사이트입니다.</p>
        <p>브롤스타즈 전적기록을 확인할 수 있고, 브롤러 승률, 조합, 카운터 등 통계 정보를 제공합니다.</p>
      </div>
    </div>
  );
}