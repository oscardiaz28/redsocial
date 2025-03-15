import React, { useState } from 'react'
import { request } from '../../libs/api'
import useAuth from '../../hooks/useAuth'
import {Link} from 'react-router-dom'
const backendUrl = import.meta.env.VITE_BACKEND_URL

export const UserList = ({
    usuarios,
    following,
    setFollowing,
    followers,
    fetchFollowing
}) => {

 const {getCounters} = useAuth()
 const [loadingMutation, setLoadingMutation] = useState(null)


 const handleFollow = async (id) => {
    setLoadingMutation(id)
    await request({
      endpoint: `/follows/${id}`,
      method: "POST",
      onSuccess: (response) => {
        setFollowing(prev => [...prev, id])
        getCounters()
        fetchFollowing && fetchFollowing()
      },
      onFailure: (err) => {
        console.log(err)
      }
    })
    setLoadingMutation(null)
  }

  const handleUnfollow = async (id) => {
    setLoadingMutation(id)
    await request({
      endpoint: `/follows/${id}`,
      method: "DELETE",
      onSuccess: (response) => {
        setFollowing( prev => prev.filter( user => user != id ) )
        getCounters()
        fetchFollowing && fetchFollowing()
        console.log(response)
      },
      onFailure: (err) => {
        console.log(err)
      }
    })
    setLoadingMutation(null)
  }

  return (
    (
        usuarios?.map( user => (
          <div className='flex items-center justify-between' key={user?._id}>
            <div className='flex items-center gap-3'>
              {
                user?.image.startsWith("avatar-default") ?
                <img src={`/avatar-default.jpg`} alt=""
                className='w-12 h-12 object-cover rounded-full my-auto' />
                : 
                <img src={`${backendUrl}/api/users/avatar/${user?.image}`} alt=""
                className='w-12 h-12 object-cover rounded-full my-auto' />
              }
              <div className='text-sm text-gray-300 flex flex-col gap-[1px]'>
                <Link to={`/profile/${user?.username}`} className='font-bold text-white'>{user?.name} {user?.surname}</Link>
                <p>{user?.position}</p>
              </div>
            </div>
            {
              following.includes(user._id) && (
                <button className='btn btn-sm bg-[#6665EC] text-white rounded-sm'
                onClick={() => handleUnfollow(user._id)}
                >
                  { loadingMutation == user._id ? 
                    <>
                      <span className="loading loading-spinner loading-xs"></span> 
                    </>
                  : "Dejar de seguir"}
                </button>
              )
            }
            {
              !following.includes(user._id) && (
                <button className='btn btn-sm btn-primary text-white rounded-sm'
                onClick={() => handleFollow(user._id)}
                >
                  {loadingMutation == user._id ? (
                    <span className="loading loading-spinner loading-xs"></span>
                  ) : followers.includes(user._id) ? (
                    "Seguir también"
                  ) : (
                    "Seguir"
                  )}
                </button>
              )
            }
          </div>
        ))
      )
  )
}
