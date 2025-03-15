import React from 'react'
import { Link } from 'react-router-dom'

export const RightPanel = () => {
  return (
    <div className=' p-4 px-6 hidden lg:block border-1 border-gray-800 rounded-md '>
      <div className=''>
        <p className='font-bold mb-6'>Usuarios Sugeridos</p>

        <div className='flex flex-col gap-6'>

          <div className='flex items-center justify-between'>
            <div className='flex items-center gap-2'>
              <img src="https://i.pinimg.com/564x/1b/a2/e6/1ba2e6d1d4874546c70c91f1024e17fb.jpg" alt=""
                className='w-10 h-10 object-cover rounded-full my-auto' />
              <div className='text-xs text-gray-300 flex flex-col gap-[1px]'>
                <Link to={"/profile/jacksmith"} className='font-bold text-white'>Jack Smith</Link>
                <p>@jacksmith</p>
              </div>
            </div>
            <button className='btn btn-sm btn-primary rounded-sm'>Follow</button>
          </div>

          <div className='flex items-center justify-between'>
            <div className='flex items-center gap-2'>
              <img src="https://i.pinimg.com/564x/1b/a2/e6/1ba2e6d1d4874546c70c91f1024e17fb.jpg" alt=""
                className='w-10 h-10 object-cover rounded-full mb-2 my-auto' />
              <div className='text-xs text-gray-300 flex flex-col gap-[1px]'>
                <Link to={"/profile/jacksmith"} className='font-bold text-white'>Jack Smith</Link>
                <p>@jacksmith</p>
              </div>
            </div>
            <button className='btn btn-sm btn-primary rounded-sm'>Follow</button>
          </div>

        </div>

      </div>
    </div>
  )
}
