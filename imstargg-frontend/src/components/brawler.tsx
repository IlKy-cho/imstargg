export function PowerLevel({value}: { value: number }) {
  return (
    <div className="relative inline-flex items-center justify-center">
      <div
        className="w-4 sm:w-6 h-4 sm:h-6 rounded-full bg-gradient-to-br from-[#E84BCE] to-[#CF30B5] flex items-center justify-center shadow-lg border-[0.5px] border-black">
        <div className="w-3 sm:w-4 h-3 sm:h-4 rounded-full bg-[#6c1473] flex items-center justify-center">
          <span className="text-white font-bold text-[0.625rem] sm:text-xs">
            {value}
          </span>
        </div>
      </div>
    </div>
  );
}