import {getEvents} from "@/lib/api/event";
import EventList from "./_components/event-list";

export default async function EventsPage() {
  const battleEvents = await getEvents();
  return (
    <div className="flex flex-col">
      <h1>이벤트 목록</h1>
      <EventList battleEvents={battleEvents}></EventList>
    </div>
  );
}