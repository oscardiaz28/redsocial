import React from 'react'
import { LuMapPin } from "react-icons/lu";
import useAuth from '../../hooks/useAuth';
import { FaLink } from "react-icons/fa6";
import {Link} from 'react-router-dom'

export const Sidebar = () => {

  const {user, counters} = useAuth()

  return (
    <div className='hidden lg:block p-4 px-6 border-1 border-gray-800 rounded-md'>
        <div className='flex flex-col items-center justify-center border-b border-gray-600 pb-6'>
          {
            user?.image.startsWith("avatar-default") ?
            <img src={`/avatar-default.jpg`} alt=""
            className='rounded-full w-30 h-30 mb-2 object-cover' />
            : 
            <img src={`http://localhost:3000/api/users/avatar/${user?.image}`} alt=""
            className='rounded-full w-30 h-30 mb-2 object-cover' />
          }
          <p className='font-bold'>{user?.name} {user?.surname}</p>
          <p className='text-gray-400 text-sm mb-2'>{user?.bio}</p>
          <p className='pt-2 font-semibold text-gray-300'>{user?.position}</p>
        </div>

        <div className='flex justify-between py-4'>
          <Link to={`siguiendo`} className='flex flex-col items-center justify-center'>
            <span>{counters?.following}</span>
            <p className='text-sm'>Siguiendo</p>
          </Link>
          <Link to={`seguidores`} className='flex flex-col items-center justify-center'>
            <span>{counters?.followers}</span>
            <p className='text-sm'>Seguidores</p>
          </Link>
          <Link to={`publicaciones`} className='flex flex-col items-center justify-center'>
            <span>{counters?.publications}</span>
            <p className='text-sm'>Publicaciones</p>
          </Link>
        </div>

        <div className='flex flex-col gap-2 py-3'>
          {user?.country == "" ? null : (
            <p className='flex items-center gap-2 text-gray-450'><LuMapPin /> { user?.country }</p>
          )}
          {user?.link == "" ? null : (
            <p className='flex items-center gap-2 text-gray-450'><FaLink /> { user?.link }</p>
          )}
        </div>

    </div>
  )
}
