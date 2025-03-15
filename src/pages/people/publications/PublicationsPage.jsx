import React, { useEffect, useState } from 'react'
import useAuth from '../../../hooks/useAuth'
import { request } from '../../../libs/api'
import { PostList } from '../../../components/common/PostList'

export const PublicationsPage = () => {

    const { user } = useAuth()
    const [posts, setPosts] = useState([])
    const [loading, setLoading] = useState(false)

    const fetchPosts = async () => {
        setLoading(true)
        await request({
            endpoint: `/publications/user/${user._id}`,
            onSuccess: (response) => {
                setPosts(response?.posts)
            },
            onFailure: (err) => {
                console.log(err)
            }
        })
        setLoading(false)
    }
    useEffect(() => {
        fetchPosts()
    }, [])

  return (
    <div className='flex flex-col'>
    
        <div className='posts flex flex-col gap-10 mt-12'>
        {loading ? <p>Loading...</p> 
        : 
            <PostList posts={posts} setPosts={setPosts} />
        }
        </div>
    </div>
  )
}
