import React from 'react'
import { FaRegComment, FaRegHeart } from 'react-icons/fa'
import { Link } from 'react-router-dom'
import { FaRegTrashAlt } from "react-icons/fa";
import useAuth from '../../hooks/useAuth';
import { request } from '../../libs/api';
import ReactTimeAgo from 'react-time-ago'

const backendUrl = import.meta.env.VITE_BACKEND_URL

export const PostList = ({posts, setPosts}) => {

    const {user} = useAuth()
    
    const handleDeletePost = async (id) => {
        if(confirm("¿Estas seguro de eliminar la publicación")){
            await request({
                endpoint: `/publications/${id}`,
                method: "DELETE",
                onSuccess: () => {
                    setPosts( prev => prev.filter( p => p._id != id ) )
                },
                onFailure: (err) => {
                    console.log(err)
                }
            })
        }
    }

  return (
    posts.map( post => (
        <div className='post relative' key={post._id}>
            {
                post?.user?._id == user._id && (
                    <button className='absolute top-2 right-0 cursor-pointer'
                    onClick={() => handleDeletePost(post._id)}
                    >
                        <FaRegTrashAlt />
                    </button>
                )
            }
          <div className='flex b-0 gap-2 items-start text-sm'>
          {
              post?.user?.image.startsWith("avatar-default") ?
                  <img src={`/avatar-default.jpg`} alt=""
                      className='w-8 h-8 object-cover rounded-full mb-2' />
                  :
                  <img src={`${backendUrl}/api/users/avatar/${post?.user?.image}`} alt=""
                      className='w-8 h-8 object-cover rounded-full mb-2' />
          }
          
            <Link to={`/profile/${post?.user?.username}`} className='font-bold'>{post?.user?.name} {post?.user?.surname}</Link>
            <p className='text-gray-400'>@{post?.user?.username}</p>
            <p className='text-gray-400'>{ <ReactTimeAgo date={post?.created_at} locale='es-ES' /> }</p>
          </div>

          <div className='pt-2'>
            <p>{post?.text}</p>
            { (post?.image && post?.image !== "" ) && (
                <img src={`${backendUrl}/api/publications/file/${post?.image}`} alt=""
                className='w-full block h-80 object-contain rounded-lg border-1 border-gray-700 mt-3'
                />
            )}
            <div className='flex gap-2 mt-3'>
              <button className='btn btn-sm btn-outline border-none rounded-sm flex items-center gap-[5px]'>
                <FaRegHeart className='text-md' />
                <p className='text-md'>12</p>
              </button>
              <button className='btn btn-sm btn-outline border-none rounded-sm flex items-center gap-[5px]'>
                <FaRegComment className='text-md' />
                <p className='text-md'>12</p>
              </button>
            </div>
          </div>
        </div>
      ))
  )

}
