import {getEvents} from "@/lib/api/event";
import EventList from "@/app/events/event-list";

export default async function EventsPage() {
  const battleEvents = await getEvents();
  return (
    <div className="flex justify-between items-center">
      <h1>이벤트 목록</h1>
      <EventList battleEvents={battleEvents}></EventList>
    </div>
  );
}