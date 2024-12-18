import { NotRegisteredEventList } from "./not-registered-event-list";

export default async function EventsPage() {
  return (
    <main>
      <div className="flex justify-between items-center">
        <h1>등록되지 않은 이벤트</h1>
      </div>
      <div>
        이곳에 표출되는 데이터는 브롤스타즈 데이터를 가공하지 않고 그대로 표출됩니다. 그러므로 존재하지 않는 코드가 존재할 수 있으며 그럴 경우 추가해야 함.
      </div>
      <NotRegisteredEventList/>
    </main>
  );
}