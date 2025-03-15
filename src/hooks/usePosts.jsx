import React, { useContext } from 'react'
import { PostContext } from '../context/PostProvider'

export const usePosts = () => {
  return useContext(PostContext)
}
