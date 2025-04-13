# Software Design - Social Media Platform with Microservices

## Overview
This repository contains the implementation of a simplified social media platform using **microservices architecture**. The platform is divided into three key microservices to ensure scalability and modularity:

1. **Authentication, Authorization & Friendship System**: Manages user authentication, roles, and user relationships.
2. **Posts, Comments & Reactions**: Handles user-generated content and interactions.
3. **Moderation & Notifications**: Manages content moderation and user notifications.

## Functionalities

### Functionality 1: Authentication & Authorization (A1)
- Users can register and log in securely.
- User passwords are encrypted and stored securely in the database.
- Authentication tokens (JWT) are generated for secure access.
- Roles (e.g., user, moderator) are assigned and verified.
- No action is allowed unless the user is authenticated.

### Functionality 2: Friendship System (A1)
- Users can send and receive friend requests.
- Requests can be accepted or rejected.
- Users can view their list of friends.
- Posts can be shared only with friends or set to public.

### Functionality 3: Posts (A2)
- Users can create posts containing text, images, and a timestamp.
- Posts are displayed in descending order by publication time (most recent first).
- Users can edit or delete their own posts.
- Posts can have hashtags for organization. If a hashtag doesn't exist, the user can create it.
- Users can filter posts by hashtag, content, or specific users.

### Functionality 4: Comments (A2)
- Users can comment on posts they can view.
- Each comment includes an author, text, an optional image, and a timestamp.
- Comments can only be edited or deleted by the author or a moderator.
- When a post is displayed individually, the associated comments are shown as well.

### Functionality 5: Reactions (A3)
- Users can react to posts and comments (e.g., Like, Love, Haha, Sad, etc.).
- A user can react only once to each post or comment but can change their reaction later.
- The total number of reactions, categorized by type, is displayed next to each post or comment.
- Comments are sorted by reactions, with the most appreciated comments appearing first.

### Functionality 6: Moderators (A3)
- Moderators have special privileges to:
  - Delete inappropriate posts or comments.
  - Block users who exhibit inappropriate behavior.
  - Unblock users if necessary.
  - Create moderator accounts.
- Users are notified when their post or comment is deleted.
- Blocked users are informed of their ban and cannot access the application.


