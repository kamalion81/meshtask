DELETE FROM public.users;
INSERT INTO public.users (id, name, date_of_birth, password) VALUES (18, 'Сергеева В.А.', '2023-06-12', '$2a$10$daoq0RrR7mrayD/c86Vtqerqa8cOpL7GJkvu094ReoSl6yYxE66H.');
INSERT INTO public.users (id, name, date_of_birth, password) VALUES (19, 'Фролов А.М.', '2023-06-12', '$2a$10$daoq0RrR7mrayD/c86Vtqerqa8cOpL7GJkvu094ReoSl6yYxE66H.');
DELETE FROM public.email_data;
INSERT INTO public.email_data (id, email, user_id) VALUES (12, 'gg@ddd.ru', 18);
INSERT INTO public.email_data (id, email, user_id) VALUES (11, 'gg@ddd.com', 19);
DELETE FROM public.phone_data;
INSERT INTO public.phone_data (id, phone, user_id) VALUES (25, '+79123456789', 18);
INSERT INTO public.phone_data (id, phone, user_id) VALUES (26, '+79123456790', 19);
DELETE FROM public.account;
INSERT INTO public.account (id, balance, user_id) VALUES (5, 100, 18);
INSERT INTO public.account (id, balance, user_id) VALUES (7, 200, 19);