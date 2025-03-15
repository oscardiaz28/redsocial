const ProfileSkeleton = () => {
	return (
		<div className='flex flex-col gap-2 w-full my-2 p-2'>
			<div className='skeleton h-6 w-1/3 ml-auto rounded-sm mb-1'></div>
			<div className='skeleton h-30 w-30 rounded-full mr-auto'></div>
			<div className="grid grid-cols-3 gap-4 mt-5 mb-2">
				<div className='skeleton h-6 w-full rounded-sm'></div>
				<div className='skeleton h-6 w-full rounded-sm'></div>
				<div className='skeleton h-6 w-full rounded-sm'></div>
			</div>
			<div className='skeleton h-6 w-1/3 rounded-sm mb-1'></div>
			<div className='skeleton h-6 w-1/2 rounded-sm'></div>
		</div>
	);
};
export default ProfileSkeleton;