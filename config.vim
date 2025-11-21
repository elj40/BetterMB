" This vim file is just to make this project a little nicer to code
set noautochdir
inoremap print System.out.println

let g:netrw_liststyle = 3
let g:netrw_winsize = 45

" Make term command open in new pane and give it a shortcut
command! -nargs=* BSplitTerm :belowright split | term <args>
nnoremap <Leader>t :BSplitTerm 

nnoremap <leader>cc :BSplitTerm ./gradlew build<cr>

autocmd VimEnter * Lexplore | vertical resize 40
