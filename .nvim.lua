-- Options
vim.opt.autochdir = false
vim.opt.wrap = false
vim.g.makeprg = "./gradlew classes"
vim.g.netrw_liststyle = 3

-- Insert mode mapping: print -> System.out.println
vim.keymap.set("i", "print", "System.out.println", { noremap = true })

-- User command: BSplitTerm
vim.api.nvim_create_user_command("BSplitTerm", function(opts)
    vim.cmd("belowright split")
    -- belowright split
    local buf = vim.api.nvim_create_buf(false, true)
    vim.api.nvim_win_set_buf(0, buf)

    local cmd = opts.args ~= "" and opts.args or vim.o.shell
    vim.fn.termopen(cmd)
end, { nargs = "*" })

-- Normal mode mappings
vim.keymap.set("n", "<Leader>t", ":BSplitTerm ", { noremap = true })
vim.keymap.set("n", "<Leader>rr", ":BSplitTerm ./gradlew :gui:run<CR>", { noremap = true, silent = true })
vim.keymap.set("n", "<Leader>dd", ":BSplitTerm ./gradlew -Dstub=true :gui:run <CR>", { noremap = true, silent = true })

-- Autocommand: open netrw explorer on startup and resize
vim.api.nvim_create_autocmd("VimEnter", {
  callback = function()
    -- Lexplore (netrw)
    vim.cmd("Lexplore")
    -- vertical resize 40
    vim.cmd("vertical resize 40")
  end,
})
