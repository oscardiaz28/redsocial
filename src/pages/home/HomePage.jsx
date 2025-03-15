import React, { useEffect, useState } from 'react'
import { MdOutlineInsertPhoto } from "react-icons/md";
import { FiSend } from "react-icons/fi";
import { Link } from 'react-router-dom';
import { FaRegHeart } from "react-icons/fa";
import { FaRegComment } from "react-icons/fa";
import useAuth from '../../hooks/useAuth';
import { CreatePost } from './components/CreatePost';
import { request } from '../../libs/api';
import { PostList } from '../../components/common/PostList';


export const HomePage = () => {

  const { user } = useAuth()
  const [posts, setPosts] = useState([])
  const [loading, setLoading] = useState(false)

  const fetchFeed = async () => {
    setLoading(true)
    await request({
        endpoint: "/publications/feed",
        onSuccess: (response) => {
            console.log(response)
            setPosts(response?.posts)
        },
        onFailure: (err) => {
            console.log(err)
        }
    })
    setLoading(false)
  }
  useEffect(() => {
    fetchFeed()
  }, [])

  return (

    <div className='flex flex-col'>

      <CreatePost user={user} setPosts={setPosts} />

      <div className='posts flex flex-col gap-10 mt-12'>
        {loading ? <p>Loading...</p> 
        : 
          <PostList posts={posts} />
        }

      </div>

    </div>
  )
}
